import {
	ActionIcon,
	Avatar,
	Box,
	Card,
	Container,
	Divider,
	Flex,
	Rating,
	Stack,
	Text,
	Title,
} from '@mantine/core'
import dayjs from 'dayjs'
import type React from 'react'
import { useEffect, useState } from 'react'
import { GiHamburgerMenu } from 'react-icons/gi'
import { Footer } from '../components/footer'
import { Sidebar } from '../components/sidebar'
import type { User } from '../types/user'

export const ProfilePage: React.FC = () => {
	const [user, setUser] = useState<User>()
	const [opened, setOpened] = useState(false)

	useEffect(() => {
		const storeUser = localStorage.getItem('user')
		if (storeUser) {
			setUser(JSON.parse(storeUser))
		}
	}, [])

	return (
		<Container size="xs" h="100vh">
			<Sidebar opened={opened} onClose={() => setOpened(false)} />
			<Stack h="100%" justify="space-between">
				<Stack gap="xs">
					<Flex
						h="70px"
						bg="teal.6"
						justify="flex-end"
						style={{ borderRadius: '10px 10px 0 0' }}
						align="center"
					>
						<ActionIcon mr={'md'} onClick={() => setOpened(true)} size={30}>
							<GiHamburgerMenu size={40} />
						</ActionIcon>
					</Flex>
					<Box ml="md">
						<Avatar
							src={user?.avatar}
							size="100px"
							style={{ position: 'absolute', top: '30px' }}
						/>
					</Box>
					<Stack justify="center" align="center" gap="xs">
						<Title order={3}>{user?.fullname}</Title>
						<Text>
							{'Subskrypcje: '}
							{user?.subscription}
						</Text>
					</Stack>
					<Divider size="xl" my="md" />
					<Stack justify="center" align="center">
						<Title order={2}>Historia wizyt</Title>
						{user?.history.map((history) => (
							<Card
								withBorder
								w="100%"
								style={{
									display: 'flex',
									flexDirection: 'row',
								}}
							>
								<Avatar
									src={history.barberName}
									size="xl"
									alt={history.id.toString()}
								/>
								<Stack justify="space-between" ml="md" w="100%">
									<Stack gap="0">
										<Text c="dimmed" size="sm">
											{dayjs(history.date).format('DD MMM YYYY')}
										</Text>
										<Title order={5}>{history.companyName}</Title>
									</Stack>
									<Flex justify="space-between" w="100%">
										<Flex gap="md">
											<Text c="dimmed" size="sm">
												Fryzjer
											</Text>
											<Text size="sm">{history.barberName}</Text>
										</Flex>
										<Rating readOnly value={history.rating} />
									</Flex>
								</Stack>
							</Card>
						))}
					</Stack>
				</Stack>
				<Footer />
			</Stack>
		</Container>
	)
}
