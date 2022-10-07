import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class HttpTokenInterceptor implements HttpInterceptor {

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        console.log(req)
        const cloned = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${localStorage.getItem('token')}`)
        });
        console.log(cloned)
        return next.handle(cloned);
    }

}
