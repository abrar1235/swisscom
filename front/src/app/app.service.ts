import { environment } from "src/environments/environment";
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http'
import { Observable, retry, catchError, throwError } from 'rxjs';
@Injectable()
export class AppService {

    private endPoint = environment.api;

    constructor(
        private httpClient: HttpClient
    ) { }

    getUpcomingMaintenance(index: any): Observable<any> {
        return this.httpClient.get<any>(`${this.endPoint}/api/v1/maintenance/getMaintenanceList?index=${index}`)
            .pipe(
                retry(3),
                catchError(this.handleError)
            );
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