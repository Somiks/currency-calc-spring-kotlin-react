import {API_BASE_URL} from '../constants/const';
import {request} from '../utils/utils';

export default class CurrencyCalculatorService {
    getCurrencyRate = (data) => {
        const params = new URLSearchParams(data).toString();
        return request({
            url: API_BASE_URL + `/calculate-rates?${params}`,
            method: 'GET'
        });
    };
    addRate = (data) => {
        return request({
            url: API_BASE_URL + "/add-rate",
            method: 'POST',
            body: JSON.stringify(data)
        });
    };
    removeRate = (id) => {
        return request({
            url: API_BASE_URL + `/remove-rate/${id}`,
            method: 'POST'
        });
    };
    listRates = () => {
        return request({
            url: API_BASE_URL + "/list-rates",
            method: 'GET'
        });
    };
}
