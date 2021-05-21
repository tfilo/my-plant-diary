import { Injectable } from '@angular/core';
import {CanActivate, Router} from "@angular/router";
import {LOCAL_STORAGE} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate {

    constructor(private router: Router) {
    }

    canActivate(): boolean {
        const tokenExpiration = Number(localStorage.getItem(LOCAL_STORAGE.TOKEN_EXPIRATION));
        if (tokenExpiration > Date.now()) {
            return true;
        }
        localStorage.removeItem(LOCAL_STORAGE.TOKEN); // remove invalid token and token expiration
        localStorage.removeItem(LOCAL_STORAGE.TOKEN_EXPIRATION);
        this.router.navigate(['login']);
        return false;
    }

}
