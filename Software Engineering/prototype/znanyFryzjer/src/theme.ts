import { createTheme } from '@mantine/core'

export const theme = createTheme({
	fontFamily: 'Montserrat, sans-serif',
	breakpoints: {
		xs: '36em',
		sm: '48em',
		md: '62em',
		lg: '75em',
		xl: '88em',
	},

	colors: {
		brand: [
			'#CCEBF0',
			'#B3E0E6',
			'#99D5DD',
			'#80CAD3',
			'#66BFCA',
			'#0D697A',
			'#0A5865',
			'#084752',
			'#05353F',
			'#03242C',
		],

		neutral: [
			'#FFFFFF',
			'#F8FAF6',
			'#F0F3EE',
			'#E8EDE5',
			'#D9DED6',
			'#CACFC7',
			'#B0B5AD',
			'#969B93',
			'#7D827A',
			'#636861',
		],

		teal: [
			'#CCEBF0',
			'#B3E0E6',
			'#99D5DD',
			'#80CAD3',
			'#66BFCA',
			'#0D697A',
			'#0A5865',
			'#084752',
			'#05353F',
			'#03242C',
		],
	},

	primaryColor: 'brand',
	primaryShade: { light: 5, dark: 4 },

	defaultRadius: 'md',

	white: '#F8FAF6',
	black: '#03242C',
})
