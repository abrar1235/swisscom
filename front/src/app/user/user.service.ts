import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, retry, throwError } from 'rxjs';

@Injectable()
export class UserService {

    private endPoint = environment.api;

    constructor(
        private httpClient: HttpClient
    ) { }

    login(credentials: any): Observable<any> {
        return this.httpClient.post<any>(`${this.endPoint}/api/v1/user/loginUser`, credentials)
            .pipe(
                retry(3),
                catchError(this.handleError)
            );
    }

    getAllUsers(index: any): Observable<any> {
        return this.httpClient.get<any>(`${this.endPoint}/api/v1/user/getAllUsers?index=${index}`)
            .pipe(
                retry(3),
                catchError(this.handleError)
            );
    }

    addUser(user: any): Observable<any> {
        return this.httpClient.post<any>(`${this.endPoint}/api/v1/user/addUser`, user)
            .pipe(
                retry(3),
                catchError(this.handleError)
            );
    }

    updateUser(user: any): Observable<any> {
        return this.httpClient.put<any>(`${this.endPoint}/api/v1/user/updateUser`, user)
            .pipe(
                retry(3),
                catchError(this.handleError)
            );
    }

    deleteUser(userId: any): Observable<any> {
        return this.httpClient.delete<any>(`${this.endPoint}/api/v1/user/deleteUser?userId=${userId}`)
            .pipe(
                retry(3),
                catchError(this.handleError)
            )
    }

    handleError(error: HttpErrorResponse) {
        if (error.status === 0) {
            // A client-side or network error occurred. Handle it accordingly.
            console.error('An error occurred:', error.error);
        } else {
            // The backend returned an unsuccessful response code.
            // The response body may contain clues as to what went wrong.
            console.error(
                `Backend returned code ${error.status}, body was: `, error.error);
        }
        // Return an observable with a user-facing error message.
        return throwError(() => new Error('Something bad happened; please try again later.'));
    }
}