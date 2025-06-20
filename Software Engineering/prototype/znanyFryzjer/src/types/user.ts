export type History = {
	id: number
	date: string
	companyName: string
	barberName: string
	rating: number
}

export type User = {
	fullname: string
	password: string
	email: string
	subscription: number
	history: History[]
	avatar: string
}
