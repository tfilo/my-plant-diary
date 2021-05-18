import {Injectable} from '@angular/core';
import {Observable, Subscription, timer} from "rxjs";
import {map} from "rxjs/operators";
import {JwtHelperService} from "@auth0/angular-jwt";
import {AuthenticateService} from "@api/api/authenticate.service";
import {Auth} from "@api/model/auth";
import {Router} from "@angular/router";
import {Token} from "@api/model/token";

interface JwtToken {
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

    private jwtToken: JwtToken | null = null;
    private token: string | null = null;
    private renewSubscription: Subscription | null = null;

    constructor(
        private as: AuthenticateService,
        private router: Router
    ) {
        this.loadAndRenewToken(); // load token when app is created
    }

    isAuthenticated(): boolean {
        return !!this.jwtToken && (this.jwtToken.exp * 1000) > Date.now();
    }

    getToken(): string | null {
        return this.token;
    }

    login(credentials: Auth): Observable<void> {
        console.log('login', credentials.username);
        return this.as.authenticateUser(credentials
        ).pipe(map((token) => {
            if (token.token) {
                this.storeToken(token.token);
            }
        }));
    }

    logout(): void {
        console.log('logout');
        this.removeToken();
        this.router.navigate(["/login"]);
    }

    private renewTimer(timestamp: number): void {
        console.log('renewTimer', timestamp);
        if (this.renewSubscription) {
            this.renewSubscription.unsubscribe();
            this.renewSubscription = null;
        }
        this.renewSubscription = timer(timestamp).subscribe(val => {
            this.renew();
        });
    }

    private renew() {
        console.log('renew');
        this.as.renewToken().subscribe(token => {
            if (token.token) {
                this.storeToken(token.token);
            }
        });
    }

    private storeToken(token: string): void {
        console.log('storeToken', token);
        localStorage.setItem('token', token);
        this.token = token;
        this.jwtToken = new JwtHelperService().decodeToken<JwtToken>(token);
        this.renewTimer(Math.max(((this.jwtToken.exp - 60) * 1000) - Date.now(), 1));
    }

    private removeToken(): void {
        console.log('removeToken');
        this.token = null;
        this.jwtToken = null;
        localStorage.removeItem('token');
        if (this.renewSubscription) {
            this.renewSubscription.unsubscribe();
            this.renewSubscription = null;
        }
    }

    private loadAndRenewToken(): void {
        console.log('loadAndRenewToken');
        this.token = localStorage.getItem('token');
        if (!!this.token) {
            this.jwtToken = new JwtHelperService().decodeToken<JwtToken>(this.token);
            if (this.isAuthenticated()) { // Token is valid
                this.renew();
            } else { // Token is expired
                this.removeToken();
            }
        }
    }
}
