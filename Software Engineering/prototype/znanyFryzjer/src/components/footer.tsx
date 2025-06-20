import { ActionIcon, Container, Group, Image } from '@mantine/core'
import type React from 'react'
import { FaInstagram, FaTwitter, FaYoutube } from 'react-icons/fa6'
import classes from './FooterSocial.module.css'

export const Footer: React.FC = () => {
	return (
		<div className={classes.footer}>
			<Container className={classes.inner}>
				<Group justify="flex-start">
					<Image
						src="/logo.svg"
						alt="logo"
						width={60}
						height={60}
						style={{
							objectFit: 'contain',
						}}
					/>
				</Group>
				<Group
					gap={0}
					className={classes.links}
					justify="flex-end"
					wrap="nowrap"
				>
					<ActionIcon size="lg" color="gray" variant="subtle">
						<FaTwitter size={18} />
					</ActionIcon>
					<ActionIcon size="lg" color="gray" variant="subtle">
						<FaYoutube size={18} />
					</ActionIcon>
					<ActionIcon size="lg" color="gray" variant="subtle">
						<FaInstagram size={18} />
					</ActionIcon>
				</Group>
			</Container>
		</div>
	)
}
