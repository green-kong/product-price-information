import axios, { AxiosError } from 'axios';


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

export const axiosPostRequestTemplate = async <T>(url: string, data: T, callback: (data: T, id: number) => void) => {
  try {
    const axiosResponse = await axios.post(BASE_URL + url, data);
    const locations = axiosResponse.headers['location'].split('/');
    const id = Number(locations[locations.length - 1]);
    callback(data, id);
  } catch (error: AxiosError<ErrorResponse>) {
    const response: ErrorResponse | undefined = error.response?.data;
    if (response) {
      alert(`ERROR_CODE : ${response.code}\nMESSAGE : ${response.message}`);
      return;
    }
    alert('ERROR_CODE : unknown\nMESSAGE : unknown error');
  }
}
