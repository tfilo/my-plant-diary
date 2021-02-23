import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html',
    styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

    registrationForm: FormGroup;

    constructor() {
        this.registrationForm = new FormGroup({
            username: new FormControl('', [Validators.minLength(5), Validators.maxLength(25), Validators.required]),
            email: new FormControl('', [Validators.minLength(5), Validators.maxLength(255), Validators.email, Validators.required]),
            firstName: new FormControl('', [Validators.maxLength(50)]),
            lastName: new FormControl('', [Validators.maxLength(50)]),
            password: new FormControl('', [Validators.minLength(8), Validators.maxLength(255), Validators.required]),
            password2: new FormControl('', [Validators.minLength(8), Validators.maxLength(255), Validators.required])
        })
    }

    ngOnInit(): void {
    }

}
