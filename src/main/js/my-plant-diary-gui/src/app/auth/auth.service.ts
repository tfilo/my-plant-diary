import {Injectable} from '@angular/core';
import {Observable, Subscription, timer} from "rxjs";
import {map} from "rxjs/operators";
import {JwtHelperService} from "@auth0/angular-jwt";
import {AuthenticateService} from "@api/api/authenticate.service";
import {Auth} from "@api/model/auth";
import {Router} from "@angular/router";

export interface JwtToken {
    enabled: boolean
    exp: number
    iat: number,
    iss: string
    sub: string
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private renewSubscription: Subscription | null = null;

    constructor(
        private as: AuthenticateService,
        private router: Router
    ) {
        this.loadAndRenewToken();
    }

    isAuthenticated(): boolean {
        const token = localStorage.getItem('token');
        if (!!token) {
            const decoded = new JwtHelperService().decodeToken<JwtToken>(token);
            if (!!decoded && (decoded.exp * 1000) > Date.now()) { // Token is valid
                return true;
            }
        }
        return false;
    }

    login(credentials: Auth): Observable<void> {
        return this.as.authenticateUser(credentials
        ).pipe(map((token) => {
            if (token.token) {
                this.storeToken(token.token);
            }
        }));
    }

    logout(): void {
        this.removeToken();
        this.router.navigate(["/login"]);
    }

    private renewTimer(timestamp: number): void {
        if (this.renewSubscription) {
            this.renewSubscription.unsubscribe();
            this.renewSubscription = null;
        }
        this.renewSubscription = timer(timestamp).subscribe(val => {
            this.renew();
        });
    }

    private renew() {
        this.as.renewToken().subscribe(token => {
            if (token.token) {
                this.storeToken(token.token);
            }
        });
    }

    private storeToken(token: string): void {
        localStorage.setItem('token', token);
        const decoded = new JwtHelperService().decodeToken<JwtToken>(token);
        this.renewTimer(Math.max(((decoded.exp - 60) * 1000) - Date.now(), 1));
    }

    private removeToken(): void {
        localStorage.removeItem('token');
        if (this.renewSubscription) {
            this.renewSubscription.unsubscribe();
            this.renewSubscription = null;
        }
    }

    private loadAndRenewToken(): void {
        const token = localStorage.getItem('token');
        if (!!token) {
            const decoded = new JwtHelperService().decodeToken<JwtToken>(token);
            if (!!decoded && (decoded.exp * 1000) > Date.now()) { // Token is valid
                this.renew();
            } else { // Token is expired
                this.removeToken();
            }
        }
    }
}
