import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {JwtHelperService} from "@auth0/angular-jwt";
import {AuthenticateService} from "@api/api/authenticate.service";
import {Auth} from "@api/model/auth";
import {Router} from "@angular/router";

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

    private tokenExpiration: number | null = null;
    private username: string | null = null;
    private token: string | null = null;

    constructor(
        private as: AuthenticateService,
        private router: Router
    ) {
    }

    isAuthenticated(): boolean {
        return !!this.token && !!this.tokenExpiration && this.tokenExpiration > Date.now();
    }

    renew(): void {
        // TODO
    }

    getToken(): string | null {
        return this.token;
    }

    login(credentials: Auth): Observable<string | null> {
        return this.as.authenticateUser(credentials
        ).pipe(map((token) => {
            if (token && token.token) {
                // TODO store token in local storage, add handler to add token to every request to server
                let decodedToken = new JwtHelperService().decodeToken<JwtToken>(token.token);
                this.tokenExpiration = decodedToken.exp * 1000;
                this.username = decodedToken.sub;
                this.token = token.token;
            }
            return this.username;
        }));
    }

    logout(): void {
        this.token = null;
        this.username = null;
        this.tokenExpiration = null;
        this.router.navigate(["/login"]);
    }
}
