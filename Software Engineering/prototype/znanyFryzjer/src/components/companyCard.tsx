import {
	Button,
	Card,
	Flex,
	Group,
	Image,
	Rating,
	Stack,
	Text,
} from '@mantine/core'
import { FaMapLocation, FaPhone } from 'react-icons/fa6'
import { TbClockHour8 } from 'react-icons/tb'
import { useNavigate } from 'react-router-dom'
import type { Company } from '../types/company'

interface CompanyCardProps {
	company: Company
}

export const CompanyCard: React.FC<CompanyCardProps> = ({ company }) => {
	const navigate = useNavigate()
	return (
		<Card p="0" withBorder>
			<Card.Section>
				<Image
					src="https://raw.githubusercontent.com/mantinedev/mantine/master/.demo/images/bg-8.png"
					height={160}
					alt={company.name}
				/>
			</Card.Section>
			<Group
				justify="space-between"
				mt="md"
				mb="xs"
				p={'xs'}
				ml={'md'}
				mr={'md'}
			>
				<Text fw={1000} fs={'xl'}>
					{company.name}
				</Text>
				<Rating value={company.rating} fractions={4} readOnly={true}></Rating>
			</Group>
			<Stack p={'xs'} gap={'0'} ml={'lg'} mr={'md'}>
				<Flex align={'center'} gap={'xs'}>
					<FaMapLocation />
					<Text>{company.address}</Text>
				</Flex>
				<Flex align={'center'} gap={'xs'}>
					<FaPhone />
					<Text>{company.phone}</Text>
				</Flex>
				<Flex align={'center'} gap={'xs'}>
					<TbClockHour8 />
					<Text>{company.workingHours}</Text>
				</Flex>
				<Flex justify={'center'} mt={'md'} mb={'xl'}>
					<Button
						variant="filled"
						radius="md"
						fullWidth
						onClick={() => navigate(`/barbershop/${company.id}`)}
					>
						Umów wizytę
					</Button>
				</Flex>
			</Stack>
		</Card>
	)
}
