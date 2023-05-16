import { Injectable, PipeTransform } from "@angular/core";
import { BehaviorSubject, Observable, Subject, Subscription, debounceTime, delay, of, switchMap, tap } from "rxjs";
import { DecimalPipe } from "@angular/common";
import { FriendProfiles, Relationship } from "../models/profiles";
import { HttpClient } from "@angular/common/http";
import { RxStompService } from "../rx-stomp/rx-stomp.service";
import { Router } from "@angular/router";

interface SearchResult {
	profiles: FriendProfiles[];
	total: number;
}

interface State {
	searchTerm: string;
}

function matches(profile: FriendProfiles, term: string, pipe: PipeTransform) {
	return (
		profile.displayName.toLowerCase().includes(term.toLowerCase())
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

	private _state: State = {
		searchTerm: '',
	};

  private URI: string = "/api";
  profiles: FriendProfiles[] = [];
  userId=1;
  sub$!: Subscription;

	constructor(private pipe: DecimalPipe,
              private httpClient: HttpClient,
			  private rxStompService: RxStompService,
			  private router:Router) {

    // this.getFriendProfiles(this.id).subscribe(x => {this.profiles = x});
    this.bossGetFriendProfiles(this.userId);

	//listen to the changes, identify by type to make changes to this.profiles
	this.sub$ = this.rxStompService
            .watch(`/notifications/profiles/${this.userId}`)
            .subscribe((data) => 
				{ let json = JSON.parse(data.body)
					switch(json['type']) {
						case "delete":
							this.onDeleteFriend(json)
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

	// getFriendProfiles(id : number){
	// 	return this.httpClient.get<FriendProfiles[]>(`${this.URI}/profiles/${this.id}`)
	// }
	
	private httpGetFriendProfiles(id : number){
		return this.httpClient.get<FriendProfiles[]>(`${this.URI}/profiles/${this.userId}`)
	}

	public bossGetFriendProfiles(id: number){
		this.httpGetFriendProfiles(id)
				.subscribe(x => {
					this.profiles = x;
					this.refreshProfiles();
				})
	}

	getRelationship(){
		return this.httpClient.get<Relationship[]>(`${this.URI}/profiles/relationship`)
	}

	addProfile(profile: FriendProfiles) {
        this.profiles.push(profile);
    }

    removeProfile(friendId: number) {
		 const relationship: Relationship = {userId1: friendId, userId2: this.userId}
		 this.httpClient.delete("/api/profiles", {body: relationship}).subscribe();
    }

	onDeleteFriend(json:any){
		const idx = this.profiles
						 	.findIndex(x => x.userId === json['profile'].userId);
						if (idx >= 0) {
						    this.profiles.splice(idx, 1);
						}
						this._search$.next();
						this.router.navigate(["/chat"])
	}

	public refreshProfiles() {
		this._search$.next();
	}
}
