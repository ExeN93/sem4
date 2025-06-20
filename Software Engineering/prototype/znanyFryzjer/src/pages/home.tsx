import { Carousel } from '@mantine/carousel'
import { ActionIcon, Container, Flex, Stack, Title } from '@mantine/core'
import Autoplay from 'embla-carousel-autoplay'
import { useRef, useState } from 'react'
import { GiHamburgerMenu } from 'react-icons/gi'
import { CompanyCard } from '../components/companyCard'
import { Footer } from '../components/footer'
import { Sidebar } from '../components/sidebar'
import { useAppContext } from '../context/useAppContextHook'

export const HomePage: React.FC = () => {
	const { companies } = useAppContext()
	const [opened, setOpened] = useState(false)
	const autoplay = useRef(Autoplay({ delay: 3000 }))

	return (
		<Container size={'xs'}>
			<Sidebar opened={opened} onClose={() => setOpened(false)} />
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
			<Stack my={'lg'} gap={'xs'}>
				<Title order={5}>Umów ponownie</Title>
				<CompanyCard company={companies[2]} />
			</Stack>
			<Stack my={'lg'} gap={'xs'}>
				<Title order={5}>Wyróżnione</Title>
				<Carousel
					slideGap="xs"
					controlsOffset="xs"
					controlSize={27}
					withControls={false}
					withIndicators
					emblaOptions={{
						loop: true,
						align: 'center',
					}}
					styles={{ indicator: { background: '#0D697A' } }}
					plugins={[autoplay.current]}
					onMouseEnter={autoplay.current.stop}
					onMouseLeave={() => autoplay.current.play()}
				>
					{companies.map((company) => (
						<Carousel.Slide key={company.id}>
							<CompanyCard company={company} />
						</Carousel.Slide>
					))}
				</Carousel>
			</Stack>
			<Footer />
		</Container>
	)
}
