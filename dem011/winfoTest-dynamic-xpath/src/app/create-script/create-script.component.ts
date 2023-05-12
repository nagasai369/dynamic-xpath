import { Component, OnInit } from '@angular/core';
import { ServiceService } from '../service.service';

@Component({
  selector: 'app-create-script',
  templateUrl: './create-script.component.html',
  styleUrls: ['./create-script.component.scss']
})
export class CreateScriptComponent implements OnInit {
  constructor(
    private createScriptService: ServiceService,
  ) {}
  ngOnInit(): void {
    // throw new Error('Method not implemented.');
  }
  actionArray =["login","navigator","openTask","sendKeys","paste","clickButton","clickCheckbox","selectByValue","clickLink","Table Dropdown Values","textarea","clickImage","clickExpandorcollapse","Dropdown Values"]
  tableData = [
    {"action":"login","inputParameter":"Oracle Applications Cloud>User ID","inputValue":"fin_impl"},{"action":"login","inputParameter":"Oracle Applications Cloud>Password","inputValue":"jGh3Y#8^"},{"action":"clickButton","inputParameter":"Sign In"},{"action":"navigator","inputParameter":"Payables>Invoices"},{"action":"clickImage","inputParameter":"Tasks"},{"action":"clickLink","inputParameter":"Create Invoice"},{"action":"clickImage","inputParameter":"Tasks"},{"action":"Dropdown Values","inputParameter":"Invoice Header>Business Unit","inputValue":"US1 Business Unit"},{"action":"sendKeys","inputParameter":" Header>Supplier","inputValue":"ABC Bank"}  ];
  scriptsJson ={
    url:"",
    xpaths:this.tableData
  }
  addRow() {
    this.tableData.push({ inputParameter: '', action: '', inputValue: '' });
  }

  removeLastRow() {
    this.tableData.pop();
  }
  scriptExecute(event: Event){
    this.createScriptService.executeScript(this.scriptsJson).subscribe();
  }

}
