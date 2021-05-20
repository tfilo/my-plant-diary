import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "@api/api/user.service";

@Component({
    selector: 'app-activate',
    templateUrl: './activate.component.html'
})
export class ActivateComponent implements OnInit {

    activationForm: FormGroup;
    username: string | null = null;

    constructor(
        private us: UserService,
        private router: Router,
        private route: ActivatedRoute
    ) {
        this.activationForm = new FormGroup({
            username: new FormControl('', [Validators.minLength(5), Validators.maxLength(25), Validators.required]),
            token: new FormControl('', [Validators.pattern("[0-9a-z]{8}\-[0-9a-z]{4}\-[0-9a-z]{4}\-[0-9a-z]{4}\-[0-9a-z]{12}"), Validators.required])
        })
    }

    ngOnInit(): void {
        this.username = this.route.snapshot.paramMap.get('username');
        if (this.username) {
            this.activationForm.patchValue({
                username: this.username
            });
        }
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
