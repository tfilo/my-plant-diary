import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Router} from "@angular/router";
import {UserService} from "@api/api/user.service";
import {CreateUser} from "@api/model/create-user";

@Component({
    selector: 'app-registration',
    templateUrl: './registration.component.html'
})
export class RegistrationComponent implements OnInit {

    registrationForm: FormGroup;

    constructor(
        private us: UserService,
        private router: Router
    ) {
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

    public submit(): void {
        if (!this.registrationForm.valid) {
            this.registrationForm.markAllAsTouched();
            return;
        }
        const cu: CreateUser = {
            email: this.registrationForm.get("email")?.value,
            firstName: this.registrationForm.get("firstName")?.value,
            lastName: this.registrationForm.get("lastName")?.value,
            password: this.registrationForm.get("password")?.value,
            username: this.registrationForm.get("username")?.value
        }

        this.us.registerUser(cu).subscribe(() => {
            this.router.navigate(['/activate']);
        });
    }

}
