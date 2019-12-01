import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import {AutoComplete, InputNumber, Select} from 'antd';
import CurrencyCalculatorService from '../../service/CurrencyCalculatorService';
import {CURRENCY_LIST} from "../../constants/const";

const {Option} = AutoComplete;


class Main extends Component {

    constructor(props) {
        super(props);
        this.state = {
            amount: '',
            fromCurrency: '',
            toCurrency: '',
            calculatedAmount: ''
        };
        this.currencyCalculatorService = new CurrencyCalculatorService();
    }

    changeValueAndCalculateAmount = (key, value) => {
        this.setState({
            [key]: value
        }, this.calculateAmount);
    };

    calculateAmount = () => {
        const amount = this.state.amount.replace(/\D/g,'');
        if (amount && this.state.fromCurrency && this.state.toCurrency) {

            this.currencyCalculatorService.getCurrencyRate({
                amount: amount,
                fromCurrency: this.state.fromCurrency,
                toCurrency: this.state.toCurrency
            }).then(response => {
                this.setState({
                    calculatedAmount: response
                })
            });
        }
    };

    filterOption = (input, option) => {
        return option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    render() {
        const currenciesItems = CURRENCY_LIST.map(currency =>
            <Option key={currency}>{currency}</Option>);

        let calculatedAmount = '';
        if (this.state.calculatedAmount) {
            calculatedAmount =
                <div id="calculatedAmount">{this.state.calculatedAmount} {this.state.toCurrency}</div>
        }

        return (
            <div className="main-container">
                <div className="filter-option">
                    <div>Amount</div>
                    <InputNumber
                        style={{width: 120}}
                        formatter={value => `$ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')}
                        parser={value => value.replace(/\$\s?|(,*)/g, '')}
                        onBlur={(event => this.changeValueAndCalculateAmount("amount", event.target.value))}
                    />
                </div>
                <div className="filter-option">
                    <div>From</div>
                    <Select
                        showSearch
                        style={{width: 80}}
                        placeholder="EUR"
                        onSelect={(value) => this.changeValueAndCalculateAmount("fromCurrency", value)}
                        filterOption={this.filterOption}
                    >
                        {currenciesItems}
                    </Select>
                </div>
                <div className="filter-option">
                    <div>To</div>
                    <Select
                        showSearch
                        style={{width: 80}}
                        placeholder="CHF"
                        onSelect={(value) => this.changeValueAndCalculateAmount("toCurrency", value)}
                        filterOption={this.filterOption}
                    >
                        {currenciesItems}
                    </Select>
                </div>
                {calculatedAmount}
            </div>
        );
    }
}

export default withRouter(Main);