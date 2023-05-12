import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceService {

  constructor(private httpClient: HttpClient) {}
 
  executeScript(scriptJson: any) {
    // debugger
    const body = { url: scriptJson.url, xpaths:scriptJson.xpaths};
    return this.httpClient.post('http://localhost:8080' + '/validate',body);
  }
}
