import axios, { AxiosError, AxiosResponse } from 'axios';

interface ErrorResponse {
  code: string;
  message: string;
}

const BASE_URL = 'http://localhost:8080/api';

const axiosGetRequestTemplate = async <T>(url: string, callBack: (data: T) => void) => {
  try {
    const {data} = await axios.get<T>(BASE_URL + url);
    callBack(data);
  } catch (error: AxiosError<ErrorResponse>) {
    const response: ErrorResponse | undefined = error.response?.data;
    if (response) {
      alert(`ERROR_CODE : ${response.code}\nMESSAGE : ${response.message}`);
      return;
    }
    alert('ERROR_CODE : unknown\nMESSAGE : unknown error');
  }
}

export { axiosGetRequestTemplate }