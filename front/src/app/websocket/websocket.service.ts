import { Injectable } from "@angular/core";
import { Client, IMessage } from "@stomp/stompjs";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import * as SockJS from 'sockjs-client';

@Injectable()
export class WebSocketService {

    private stompClient?: Client;

    connect(token: any, enableDebug: boolean = false): Observable<any> {
        return new Observable(res => {
            this.stompClient = new Client({
                brokerURL: `ws:${environment.webSocketUrl}`,
                webSocketFactory: () => new SockJS(`http:${environment.webSocketUrl}`),
                heartbeatIncoming: 2000,
                heartbeatOutgoing: 2000,
                reconnectDelay: 5000,
                debug: (e) => {
                    if (enableDebug)
                        console.log(e)
                },
                connectHeaders: {
                    token: token
                }
            });
            this.stompClient.activate();
            let interval = setInterval(() => {
                if (this.stompClient?.connected) {
                    clearInterval(interval);
                    return res.next(true);
                }
            }, 250);
        });
    }

    subscribe(endPoint: any): Observable<IMessage> {
        return new Observable(res => {
            this.stompClient?.subscribe(endPoint, e => {
                res.next(e);
            });
        });
    }

    publish(endPoint: any, payLoad: any) {
        this.stompClient?.publish({
            destination: endPoint,
            body: JSON.stringify(payLoad)
        });
    }
}