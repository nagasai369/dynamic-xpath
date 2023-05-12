import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateScriptComponent } from './create-script/create-script.component';

const routes: Routes = [
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
