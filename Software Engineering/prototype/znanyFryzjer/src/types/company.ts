import type { User } from './user'

export type Company = {
	id: number
	name: string
	address: string
	phone: string
	email: string
	employees: User[]
	logo: string
	rating: number
	workingHours: string
}
