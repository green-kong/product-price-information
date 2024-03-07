import axios, { AxiosError } from 'axios';
import { Product } from '@apis/get/RequestGeTableCellllProducts';


interface SuccessResponse<T> {
  isSuccess: true;
  response: T;
}

interface FailResponse {
  isSuccess: false;
  response: undefined;
}

export type ApiResponse<T> = SuccessResponse<T> | FailResponse;


interface ErrorResponse {
  code: string;
  message: string;
}

const BASE_URL = 'http://localhost:8080/api';

export const axiosGetRequestTemplate = async <T>(url: string): Promise<T> => {
  try {
    const {data} = await axios.get<T>(BASE_URL + url);
    return data;
  } catch (error: AxiosError<ErrorResponse>) {
    const response: ErrorResponse | undefined = error.response?.data;
    if (response) {
      alert(`ERROR_CODE : ${response.code}\nMESSAGE : ${response.message}`);
      return;
    }
    alert('ERROR_CODE : unknown\nMESSAGE : unknown error');
  }
}

export const axiosPostRequestTemplate = async <T>(url: string, data: T): Promise<number> => {
  try {
    const axiosResponse = await axios.post(BASE_URL + url, data);
    const locations = axiosResponse.headers['location'].split('/');
    return Number(locations[locations.length - 1]);
  } catch (error: AxiosError<ErrorResponse>) {
    const response: ErrorResponse | undefined = error.response?.data;
    if (response) {
      alert(`ERROR_CODE : ${response.code}\nMESSAGE : ${response.message}`);
      return;
    }
    alert('ERROR_CODE : unknown\nMESSAGE : unknown error');
  }
};

export const axiosDeleteRequestTemplate = async (url): Promise<boolean> => {
  try {
    await axios.delete(BASE_URL + url);
    return true;
  } catch (error: AxiosError<ErrorResponse>) {
    const response: ErrorResponse | undefined = error.response?.data;
    if (response) {
      alert(`ERROR_CODE : ${response.code}\nMESSAGE : ${response.message}`);
      return false;
    }
    alert('ERROR_CODE : unknown\nMESSAGE : unknown error');
  }
}

export const axiosPatchProductPriceRequestTemplate = async <T>(url: string, data: T): Promise<boolean> => {
  try {
    await axios.patch(BASE_URL + url, data);
    return true;
  } catch (error: AxiosError<ErrorResponse>) {
    const response: ErrorResponse | undefined = error.response?.data;
    if (response) {
      alert(`ERROR_CODE : ${response.code}\nMESSAGE : ${response.message}`);
      return false;
    }
    alert('ERROR_CODE : unknown\nMESSAGE : unknown error');
  }
}
