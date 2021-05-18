import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {UserService} from "@api/api/user.service";

@Component({
    selector: 'app-activate',
    templateUrl: './activate.component.html',
    styleUrls: ['./activate.component.scss']
})
export class ActivateComponent implements OnInit {

    activationForm: FormGroup;

    constructor(
        private us: UserService,
        private router: Router
    ) {
        this.activationForm = new FormGroup({
            username: new FormControl('', [Validators.minLength(5), Validators.maxLength(25), Validators.required]),
            token: new FormControl('', [Validators.pattern("[0-9a-z]{8}\-[0-9a-z]{4}\-[0-9a-z]{4}\-[0-9a-z]{4}\-[0-9a-z]{12}"), Validators.required])
        })
    }

    ngOnInit(): void {
    }

    public submit(): void {
        if (!this.activationForm.valid) {
            this.activationForm.markAllAsTouched();
            return;
        }

        this.us.activateUser(this.activationForm.value).subscribe(() => {
            this.router.navigate(['/login']);
        });
    }
}
