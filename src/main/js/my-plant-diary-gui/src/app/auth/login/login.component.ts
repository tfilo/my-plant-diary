import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from "../auth.service";
import {Router} from "@angular/router";

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    loginForm: FormGroup;

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
        this.loginForm = new FormGroup({
            username: new FormControl('', [Validators.maxLength(25), Validators.required]),
            password: new FormControl('', [Validators.maxLength(255), Validators.required])
        })
    }

    ngOnInit(): void {
    }

    public login(): void {
        this.authService.login(this.loginForm.value).subscribe(() => {
                this.router.navigate(['/dashboard']);
        });
    }
}
