import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { catchError, Observable, retry, throwError } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable()
export class MaintenanceService {

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

    addMaintenance(maintenance: any): Observable<any> {
        return this.httpClient.post<any>(`${this.endPoint}/api/v1/maintenance/addMaintenance`, maintenance)
            .pipe(
                retry(3),
                catchError(this.handleError)
            )
    }

    deleteMaintenance(maintenanceId: any): Observable<any> {
        return this.httpClient.delete<any>(`${this.endPoint}/api/v1/maintenance/deleteMaintenance?maintenanceId=${maintenanceId}`)
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