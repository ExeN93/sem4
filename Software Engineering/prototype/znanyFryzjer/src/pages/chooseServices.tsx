import {
	ActionIcon,
	Avatar,
	Box,
	Button,
	Card,
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

export const ChooseServices: React.FC = () => {
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

					<Stack w="100%" mt="md" gap="xs">
						<Title order={4}>Usługi fryzjerskie</Title>
						<Stack gap="md">
							<Card withBorder w="100%">
								<Stack>
									<Flex gap="xs">
										<Title order={5}>Strzyżenie męskie</Title>
										<Text size="sm" c="dimmed">
											1 god.
										</Text>
									</Flex>
									<Text fw={700} c="teal.4">
										80zł
									</Text>
								</Stack>
							</Card>
							<Card withBorder w="100%">
								<Stack>
									<Flex gap="xs">
										<Title order={5}>Strzyżenie głowy + broda</Title>
										<Text size="sm" c="dimmed">
											1.5 god.
										</Text>
									</Flex>
									<Text fw={700} c="teal.4">
										200zł
									</Text>
								</Stack>
							</Card>
							<Card withBorder w="100%">
								<Stack>
									<Flex gap="xs">
										<Title order={5}>Strzyżenie męskie</Title>
										<Text size="sm" c="dimmed">
											1 god.
										</Text>
									</Flex>
									<Text fw={700} c="teal.4">
										80zł
									</Text>
								</Stack>
							</Card>
							<Card withBorder w="100%">
								<Stack>
									<Flex gap="xs">
										<Title order={5}>Strzyżenie głowy + broda</Title>
										<Text size="sm" c="dimmed">
											1.5 god.
										</Text>
									</Flex>
									<Text fw={700} c="teal.4">
										180zł
									</Text>
								</Stack>
							</Card>
						</Stack>
						<Button
							size="lg"
							onClick={() => navigate(`/barbershop/${id}/time`)}
						>
							Wybierz czas
						</Button>
					</Stack>
				</Stack>
				<Footer />
			</Stack>
		</Container>
	)
}
