import { Injectable, PipeTransform } from "@angular/core";
import { firstValueFrom, BehaviorSubject, Observable, Subject, Subscription, debounceTime, delay, map, of, switchMap, take, tap, exhaustMap } from "rxjs";
import { DecimalPipe } from "@angular/common";
import { FriendProfiles, Relationship } from "../models/profiles";
import { HttpClient, HttpParams } from "@angular/common/http";
import { RxStompService } from "../rx-stomp/rx-stomp.service";
import { Router } from "@angular/router";
import { AuthService } from "./auth.service";


interface SearchResult {
	profiles: FriendProfiles[];
	total: number;
}

interface State {
	searchTerm: string;
}

function matches(profile: FriendProfiles, term: string, pipe: PipeTransform) {
	return (
		profile.username.toLowerCase().includes(term.toLowerCase())
	);
}


@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private _loading$ = new BehaviorSubject<boolean>(true);
	private _search$ = new Subject<void>();
	private _profiles$ = new BehaviorSubject<FriendProfiles[]>([]);
	private _total$ = new BehaviorSubject<number>(0);
	private _profile$ = new BehaviorSubject<FriendProfiles|null>(null);
	private _state: State = {
		searchTerm: '',
	};

  private URI: string = "/api";
  profiles: FriendProfiles[] = [];
  userId!:number;
  sub$!: Subscription;
//   profile!: FriendProfiles;
 

	constructor(private pipe: DecimalPipe,
              private httpClient: HttpClient,
			  private rxStompService: RxStompService,
			  private router: Router, 
			  private authService: AuthService) {

	this.authService.token$
				.subscribe(user => {
					if (user) this.userId = user.userId;
					this.getFriendProfiles(this.userId);
				}
				);


	//listen to the changes, identify by type to make changes to this.profiles
	this.sub$ = this.rxStompService
            .watch(`/notifications/profiles/${this.userId}`)
            .subscribe((data) => 
				{ let json = JSON.parse(data.body);
					// console.log(json)
					switch(json['type']) {
						case "delete":
							this.onDeleteFriend(json)
							break;
						case "add":
							this.onAddProfile(json)
							break;
						case "message":
							this.onNewMsg(json)
							break;
						case "edit":
							this.onEditName(json)
							break;
						default:
							console.log(json)
							break;
					}
                                });

		this._search$
			.pipe(
				tap(() => this._loading$.next(true)),
				debounceTime(200),
				switchMap(() => this._search()),
				delay(200),
				tap(() => this._loading$.next(false)),
			)
			.subscribe((result:any) => {
				this._profiles$.next(result.profiles);
				this._total$.next(result.total);
			});

		this._search$.next();
	}

	get profiles$() {
		return this._profiles$.asObservable();
	}

	// get profile$() {
	// 	return this._profile$.asObservable();
	// }

	get total$() {
		return this._total$.asObservable();
	}
	get loading$() {
		return this._loading$.asObservable();
	}
	get searchTerm() {
		return this._state.searchTerm;
	}
	set searchTerm(searchTerm: string) {
		this._set({ searchTerm });
	}

	private _set(patch: Partial<State>) {
		Object.assign(this._state, patch);
		this._search$.next();
	}

	private _search(): Observable<SearchResult> {
		const { searchTerm } = this._state;

		// 1. sort
		let profiles = this.profiles;

		// 2. filter
		profiles = profiles.filter((profile) => matches(profile, searchTerm, this.pipe));
		const total = profiles.length;

		return of({ profiles, total });
	}
	
	private httpGetFriendProfiles(id : number){
		return this.httpClient.get<FriendProfiles[]>(`${this.URI}/profiles/${id}`)
	}

	public getFriendProfiles(id: number){
		this.httpGetFriendProfiles(id)
				.subscribe(x => {
					this.profiles = x.sort((a, b) => {
						const timeA = new Date(a.msgTime).getTime();
						const timeB = new Date(b.msgTime).getTime();
						return timeB - timeA;
					  });
					this.refreshProfiles();
				})
	}

	getRelationship(){
		return this.httpClient.get<Relationship[]>(`${this.URI}/profiles/relationship`)
	}

	addProfile(id:number, payload: {email: string}) {
        return this.httpClient.post(`${this.URI}/profiles/${id}/addfriend`,payload);
    }

    removeProfile(friendId: number) {
		 const relationship: Relationship = {userId1: friendId, userId2: this.userId}
		 this.httpClient.delete(`${this.URI}/profiles`, {body: relationship}).subscribe();
    }

	upDateNameById(id:number, payload:{name:string}){
		return this.httpClient.put(`${this.URI}/profiles/${id}`,payload);
	}

	onDeleteFriend(json:any){
		const idx = this.profiles
						 	.findIndex(x => x.userId === json['profile'].userId);
						if (idx >= 0) {
						    this.profiles.splice(idx, 1);
						}
						this.refreshProfiles();
						this.router.navigate(["/chat"])
	}

	onAddProfile(json:any){
		this.profiles.unshift(json['profile']);
		this.refreshProfiles();
	}

	onNewMsg(json:any){
		const idx = this.profiles
			.findIndex(x => x.userId === json['profile'].userId);
		if (idx >= 0) {
			this.profiles.splice(idx, 1);
			this.profiles.unshift(json['profile'])
		}
		this.refreshProfiles();
	}

	onEditName(json:any){
		const idx = this.profiles
			.findIndex(x => x.userId === json['profile'].userId);
		if (idx >= 0) {
			//push back to the same index
			this.profiles.splice(idx, 1,json['profile']);
		}
		this.refreshProfiles();
	}

	getProfileById(userId:number){
		return this.httpClient.get<{username:string,imageUrl:string}>(`${this.URI}/profiles/userprofile/${userId}`);
	}

	
	public refreshProfiles() {
		this._search$.next();
	}
}
