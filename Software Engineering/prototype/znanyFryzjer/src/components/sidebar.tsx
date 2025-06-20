import { Button, Drawer, Stack } from '@mantine/core'
import type React from 'react'
import { useNavigate } from 'react-router-dom'
import ThemeSwitch from '../components/themeSwitcher'

interface SidebarProps {
	opened: boolean
	onClose: () => void
}

export const Sidebar: React.FC<SidebarProps> = ({ opened, onClose }) => {
	const navigate = useNavigate()
	return (
		<Drawer
			opened={opened}
			onClose={onClose}
			overlayProps={{ backgroundOpacity: 0.5, blur: 4 }}
			position="right"
		>
			<Stack justify="space-between" h="100%">
				<ThemeSwitch />
				<Stack gap={'xs'} h={'100%'}>
					<Button.Group orientation="vertical">
						<Button variant="default" onClick={() => navigate('/home')}>
							Główna
						</Button>
						<Button variant="default" onClick={() => navigate('/profile')}>
							Mój profil
						</Button>
						<Button variant="default">Barbershopy</Button>
					</Button.Group>
				</Stack>
				<Button
					onClick={() => {
						localStorage.setItem('user', '')
						navigate('/')
					}}
				>
					Wyloguj się
				</Button>
			</Stack>
		</Drawer>
	)
}
