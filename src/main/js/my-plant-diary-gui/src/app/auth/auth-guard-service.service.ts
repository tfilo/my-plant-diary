import { Injectable } from '@angular/core';
import {CanActivate, Router} from "@angular/router";
import {JwtToken} from "./auth.service";
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(private router: Router) {
    }

    canActivate(): boolean {
        const token = localStorage.getItem('token');
        if (token) {
            const decoded = new JwtHelperService().decodeToken<JwtToken>(token);
            if (!!decoded && (decoded.exp * 1000) > Date.now()) {
                return true;
            }
        }
        localStorage.removeItem('token'); // remove invalid token
        this.router.navigate(['login']);
        return false;
    }

}
