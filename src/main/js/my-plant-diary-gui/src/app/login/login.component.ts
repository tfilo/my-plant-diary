import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    loginForm: FormGroup;

    constructor() {
        this.loginForm = new FormGroup({
            username: new FormControl('', [Validators.maxLength(25), Validators.required]),
            password: new FormControl('', [Validators.maxLength(255), Validators.required])
        })
    }

    ngOnInit(): void {}

    public login(): void {
        console.log(this.loginForm.value);
    }
}
