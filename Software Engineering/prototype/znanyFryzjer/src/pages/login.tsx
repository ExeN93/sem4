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
import { useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { Footer } from '../components/footer'
import { useAppContext } from '../context/useAppContextHook'

export const LoginPage: React.FC = () => {
	const { users } = useAppContext()
	const navigate = useNavigate()

	useEffect(() => {
		console.log(users)
	}, [users])

	const form = useForm({
		mode: 'uncontrolled',
		initialValues: {
			email: '',
			password: '',
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
		const user = users.find(
			(user) =>
				user.email === form.getValues().email &&
				user.password === form.getValues().password
		)
		if (!user) {
			return
		}

		localStorage.setItem('user', JSON.stringify(user))
		navigate('/home')
	}

	useEffect(() => {
		console.log(users)
	}, [users])

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
										Logowanie
									</Text>
								</Center>
								<form onSubmit={form.onSubmit(handleSubmit)}>
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
									<Button type="submit" fullWidth mt="md">
										Zaloguj się
									</Button>
								</form>
								<Flex justify={'center'} align={'center'} pt={'md'}>
									<Text>Nie masz konta?</Text>
									<Text
										style={{ cursor: 'pointer', textDecoration: 'underline' }}
										ml="xs"
										variant="subtle"
										onClick={() => navigate('/register')}
										c="teal.6"
									>
										Zarejestruj się
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
