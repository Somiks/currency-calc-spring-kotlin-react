import './Admin.css';
import React, {Component} from 'react';
import {withRouter} from "react-router-dom";
import {Select, Button, Form, Input, message} from "antd";
import {CURRENCY_LIST} from "../../constants/const";
import CurrencyCalculatorService from "../../service/CurrencyCalculatorService";

const {Option} = Select;
const FormItem = Form.Item;
const NUMBERS_WITH_DIGIT_REGEXP = new RegExp('^\\d*\\.?\\d*$');

class Admin extends Component {

    constructor(props) {
        super(props);
        this.state = {
            fromCurrency: {
                value: ''
            },
            toCurrency: {
                value: ''
            },
            fee: {
                value: ''
            },
            fromCurrenciesFilter: CURRENCY_LIST,
            toCurrenciesFilter: CURRENCY_LIST,
            rates: []
        };
        this.currencyCalculatorService = new CurrencyCalculatorService();
    }

    componentDidMount() {
        this.currencyCalculatorService.listRates().then(response => {
            this.setState({
                rates : response
            });
        });
    }

    handleSubmit = (event) => {
        event.preventDefault();
        const postData = {
            fromCurrency: this.state.fromCurrency.value,
            toCurrency: this.state.toCurrency.value,
            fee: this.state.fee.value,
        };
        this.currencyCalculatorService.addRate(postData).then(response => {
            const rates = this.state.rates;
            rates.push(response);
            this.setState({
                rates : rates
            });
        });
    };

    validateInput(name, value, validationFun, executeAfterSetState) {
        this.setState({
            [name]: {
                value: value,
                ...validationFun(value)
            }
        }, executeAfterSetState);
    };

    setAndValidateCurrency(name) {
        const value = this.state[name].value;
        this.setState({
            [name]: {
                value: value,
                ...this.validateCurrency(value)
            }
        }, this.globalValidate);
    };

    globalValidate = () => {
        if (this.isAlreadyAddedRate()) {
            message.error('Entry already exist ' + this.state.fromCurrency.value + ' ' + this.state.toCurrency.value)
        }
    };

    validateFee = (fee) => {
        if (fee.length < 3 || !NUMBERS_WITH_DIGIT_REGEXP.test(fee)) {
            return {
                validateStatus: 'error'
            }
        }
        return this.successStatus();
    };
    successStatus = () => {
        return {
            validateStatus: 'success',
            errorMsg: ''
        }
    };

    validateCurrency = (value) => {
       if (!value) {
            return {
                validateStatus: 'error'
            }
        }
        return this.successStatus();
    };

    isFormInvalid() {
        return !(
            this.state.fromCurrency.validateStatus === 'success' &&
            this.state.toCurrency.validateStatus === 'success' &&
            this.state.fee.validateStatus === 'success' &&
            !this.isAlreadyAddedRate()
        );
    };

    isAlreadyAddedRate() {
        return this.state.rates.some(rate =>
            rate.fromCurrency === this.state.fromCurrency.value &&
            rate.toCurrency === this.state.toCurrency.value);
    }

    changeState = (name, value) => {
        this.setState({
            [name]: {
                value: value
            }
        });
    };

    filterOption = (input, option) => {
        return option.props.children.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    };

    removeRate = (id) => {
        this.currencyCalculatorService.removeRate(id).then(response => {
            const filteredList = this.state.rates.filter(rate => rate.id !== id);
            this.setState({
                rates : filteredList
            });
        });
    };
    render() {
        const currenciesItems = CURRENCY_LIST.map(currency => <Option
            key={currency}>{currency}</Option>);
        const rates = this.state.rates.map(rate =>
            <div key={rate.fromCurrency + rate.toCurrency} className="row">
                <div className="text-coll">{rate.fromCurrency}</div>
                <div className="text-coll">{rate.toCurrency}</div>
                <div className="text-coll">{rate.fee}</div>
                <div>
                    <Button type="danger" onClick={() => this.removeRate(rate.id)}>
                        REMOVE
                    </Button>
                </div>
            </div>

        );
        return (
            <div className="admin-container">

                <Form onSubmit={this.handleSubmit}>
                    <div className="rates-table">
                        <div className="row">
                            <FormItem
                                label="From"
                                validateStatus={this.state.fromCurrency.validateStatus}>
                                <Select
                                    showSearch
                                    style={{width: 80}}
                                    placeholder="EUR"
                                    onSelect={(value) => this.changeState("fromCurrency", value)}
                                    onBlur={() => this.setAndValidateCurrency("fromCurrency")}
                                    filterOption={this.filterOption}
                                >
                                    {currenciesItems}
                                </Select>
                            </FormItem>
                            <FormItem
                                label="To"
                                validateStatus={this.state.toCurrency.validateStatus}>
                                <Select
                                    showSearch
                                    style={{width: 80}}
                                    placeholder="EUR"
                                    onSelect={(value) => this.changeState("toCurrency", value)}
                                    onBlur={() => this.setAndValidateCurrency("toCurrency")}
                                    filterOption={this.filterOption}
                                >
                                    {currenciesItems}
                                </Select>
                            </FormItem>
                            <FormItem
                                label="Fee"
                                validateStatus={this.state.fee.validateStatus}>
                                <Input
                                    id="fee"
                                    autoComplete="off"
                                    placeholder="0.1"
                                    name="fee"
                                    onChange={(event) => this.validateInput(event.target.name, event.target.value,  this.validateFee)}
                                />
                            </FormItem>
                            <FormItem>
                                <Button type="primary"
                                        htmlType="submit"
                                        size="large"
                                        disabled={this.isFormInvalid()}>ADD</Button>
                            </FormItem>
                        </div>
                        {rates}
                    </div>
                </Form>

            </div>
        );
    }
}

export default withRouter(Admin);