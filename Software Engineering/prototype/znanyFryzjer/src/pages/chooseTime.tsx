import {
	ActionIcon,
	Avatar,
	Box,
	Button,
	Container,
	Divider,
	Flex,
	Stack,
	Text,
	Title,
} from '@mantine/core'
import type React from 'react'
import { useState } from 'react'
import { FaAngleDown } from 'react-icons/fa6'
import { GiHamburgerMenu } from 'react-icons/gi'
import { useNavigate, useParams } from 'react-router-dom'
import { Footer } from '../components/footer'
import { Sidebar } from '../components/sidebar'
import { useAppContext } from '../context/useAppContextHook'

export const ChooseTime: React.FC = () => {
	const { id } = useParams()
	const [opened, setOpened] = useState(false)
	const { companies } = useAppContext()
	const navigate = useNavigate()

	return (
		<Container size="xs" h="100vh">
			<Sidebar opened={opened} onClose={() => setOpened(false)} />
			<Stack h="100%" justify="space-between">
				<Stack>
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
								src={companies[Number(id)].logo}
								size="100px"
								style={{ position: 'absolute', top: '20px' }}
							/>
						</Box>
					</Stack>
					<Stack gap="0" mt="xl">
						<Flex align={'center'}>
							<Title order={2}>{companies[Number(id)].name}</Title>
							<ActionIcon flex={'flex-end'} ml={'xl'} variant="transparent">
								<FaAngleDown size={0} />
							</ActionIcon>
						</Flex>
						<Text c="dimmed" size="sm">
							{companies[Number(id)].address}
						</Text>
						<Text c="dimmed" size="sm">
							{`pon-sob ${companies[Number(id)].workingHours}`}
						</Text>
					</Stack>
					<Divider size="xl" my="md" />

					<Stack w="100%" mt="md" gap="xs" justify="space-between">
						<Flex w="100%" align="center">
							<Title order={4}>Wybierz date</Title>
							<Text ml="300px" c="teal.4" fw={700}>
								Czerwiec
							</Text>
						</Flex>
						<Stack gap="md" justify="space-between">
							<Button.Group orientation="horizontal">
								<Button variant="default">11</Button>
								<Button variant="default">12</Button>
								<Button variant="default">13</Button>
								<Button variant="default">14</Button>
								<Button variant="default">15</Button>
								<Button variant="default">16</Button>
								<Button variant="default">17</Button>
								<Button variant="default">18</Button>
								<Button variant="default">19</Button>
							</Button.Group>
							<Title order={4}>Rano</Title>
							<Button.Group orientation="horizontal">
								<Button variant="default">08:30</Button>
								<Button variant="default">09:00</Button>
								<Button variant="default">09:30</Button>
								<Button variant="default">10:00</Button>
								<Button variant="default">10:30</Button>
								<Button variant="default">11:00</Button>
								<Button variant="default">11:30</Button>
							</Button.Group>
							<Button.Group orientation="horizontal">
								<Button variant="default">12:00</Button>
								<Button variant="default">12:30</Button>
								<Button variant="default">13:00</Button>
								<Button variant="default">13:30</Button>
								<Button variant="default">14:00</Button>
								<Button variant="default">14:30</Button>
								<Button variant="default">15:00</Button>
							</Button.Group>
							<Title order={4}>Po poludnie</Title>
							<Button.Group orientation="horizontal">
								<Button variant="default">15:30</Button>
								<Button variant="default">16:00</Button>
								<Button variant="default">16:30</Button>
								<Button variant="default">17:00</Button>
								<Button variant="default">17:30</Button>
								<Button variant="default">18:00</Button>
								<Button variant="default">18:30</Button>
							</Button.Group>
							<Button.Group orientation="horizontal">
								<Button variant="default">17:30</Button>
								<Button variant="default">18:00</Button>
								<Button variant="default">18:30</Button>
								<Button variant="default">19:00</Button>
								<Button variant="default">19:30</Button>
							</Button.Group>
						</Stack>
					</Stack>
					<Button size="lg" mt="100px" onClick={() => navigate('/home')}>
						Zarezerwuj
					</Button>
				</Stack>
				<Footer />
			</Stack>
		</Container>
	)
}
