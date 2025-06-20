import { createContext, useState } from 'react'
import type { User } from '../types/user'
import type { Company } from '../types/company'
import { companiesdb } from '../database/companies'

type AppContextType = {
	users: User[]
	setUsers: React.Dispatch<React.SetStateAction<User[]>>
	companies: Company[]
	setCompanies: React.Dispatch<React.SetStateAction<Company[]>>
}

// eslint-disable-next-line react-refresh/only-export-components
export const AppContext = createContext<AppContextType | undefined>(undefined)

export const AppProvider: React.FC<React.PropsWithChildren> = ({
	children,
}) => {
	const [users, setUsers] = useState<User[]>([])
	const [companies, setCompanies] = useState<Company[]>(companiesdb)

	return (
		<AppContext.Provider value={{ users, setUsers, companies, setCompanies }}>
			{children}
		</AppContext.Provider>
	)
}
