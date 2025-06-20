import {
	ActionIcon,
	Avatar,
	Box,
	Card,
	Container,
	Divider,
	Flex,
	Image,
	Stack,
	Text,
	Title,
} from '@mantine/core'
import { useState } from 'react'
import { FaAngleDown } from 'react-icons/fa6'
import { GiHamburgerMenu } from 'react-icons/gi'
import { useNavigate, useParams } from 'react-router-dom'
import { Footer } from '../components/footer'
import { Sidebar } from '../components/sidebar'
import { useAppContext } from '../context/useAppContextHook'

export const BarbershopPage: React.FC = () => {
	const { id } = useParams()
	const { companies } = useAppContext()
	const [opened, setOpened] = useState(false)
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
					<Stack gap="xs">
						<Title order={4}>Tnij jak ostatnio</Title>
						<Image
							h={200}
							src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-8.png"
						/>
					</Stack>
					<Stack w="100%" mt="md" gap="xs">
						<Title order={4}>Zapisy online</Title>
						<Flex gap="md">
							<Card
								withBorder
								style={{ justifyContent: 'center', alignItems: 'center' }}
								w="50%"
								h="200px"
							>
								<Stack justify="center" align="center">
									<Image h={80} w={80} src="/fryzjer.png" />
									<Text fw={700}>Wybierz fryzjera</Text>
								</Stack>
							</Card>
							<Card
								withBorder
								style={{ justifyContent: 'center', alignItems: 'center' }}
								w="50%"
								onClick={() => navigate(`/barbershop/${id}/services`)}
							>
								<Stack justify="center" align="center">
									<Image h={80} w={80} src="/usluga.png" />
									<Text fw={700}>Wybierz usługę</Text>
								</Stack>
							</Card>
						</Flex>
					</Stack>
				</Stack>
				<Footer />
			</Stack>
		</Container>
	)
}

export default BarbershopPage
