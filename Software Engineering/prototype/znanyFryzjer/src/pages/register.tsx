import {
	Box,
	Button,
	Center,
	Container,
	Flex,
	Group,
	Paper,
	PasswordInput,
	Stack,
	Text,
	TextInput,
	Title,
} from '@mantine/core'
import { useForm } from '@mantine/form'
import type React from 'react'
import { useNavigate } from 'react-router-dom'
import { Footer } from '../components/footer'
import { useAppContext } from '../context/useAppContextHook'
import type { User } from '../types/user'

function randomDate(start: Date, end: Date) {
	return new Date(
		start.getTime() + Math.random() * (end.getTime() - start.getTime())
	)
}

export const RegisterPage: React.FC = () => {
	const { setUsers } = useAppContext()
	const navigate = useNavigate()
	const form = useForm({
		mode: 'uncontrolled',
		initialValues: {
			email: '',
			password: '',
			fullname: '',
		},
		validate: {
			email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email'),
		},
	})

	const handleSubmit = () => {
		const error = form.validate()
		if (error.hasErrors) {
			return
		}
		setUsers((prev: User[]) => [
			...prev,
			{
				fullname: form.getValues().fullname,
				email: form.getValues().email,
				password: form.getValues().password,
				subscription: Math.floor(Math.random() * 100),
				avatar:
					'https://img.freepik.com/premium-vector/character-avatar-isolated_729149-194801.jpg?semt=ais_hybrid&w=740',
				history: [
					{
						id: 1,
						date: randomDate(new Date(2025, 0, 1), new Date()).toString(),
						companyName: 'Fryzjernia1',
						barberName: 'Bartosz',
						rating: Math.floor(Math.random() * 5),
					},
				],
			},
		])

		navigate('/')
	}

	return (
		<Container size="xs">
			<Center mih="100vh">
				<Stack w="100%" mih="100vh" justify="space-between">
					<Box h="30px"></Box>
					<Group w="100%">
						<Center w="100%" mb="lg">
							<Title order={1} c="teal.6">
								Znany Fryzjer
							</Title>
						</Center>
						<Paper withBorder p="xl" radius="md" w="100%">
							<Stack gap="md">
								<Center>
									<Text size="xl" fw={700}>
										Rejestracja
									</Text>
								</Center>
								<form onSubmit={form.onSubmit(handleSubmit)}>
									<TextInput
										label="Imię"
										placeholder="Imię"
										required
										key={form.key('fullname')}
										{...form.getInputProps('fullname')}
									/>
									<TextInput
										label="Email"
										placeholder="Email"
										required
										key={form.key('email')}
										{...form.getInputProps('email')}
									/>
									<PasswordInput
										label="Hasło"
										placeholder="Hasło"
										required
										key={form.key('password')}
										{...form.getInputProps('password')}
									/>
									<Button fullWidth mt="md" type="submit">
										Zarejestruj się
									</Button>
								</form>
								<Flex justify={'center'} align={'center'} pt={'md'}>
									<Text>Masz konto?</Text>
									<Text
										style={{ cursor: 'pointer', textDecoration: 'underline' }}
										ml="xs"
										variant="subtle"
										onClick={() => navigate('/')}
										c="teal.6"
									>
										Zaloguj się
									</Text>
								</Flex>
							</Stack>
						</Paper>
					</Group>
					<Footer />
				</Stack>
			</Center>
		</Container>
	)
}
