export default {
  plugins: {
    autoprefixer: {},
    'postcss-px-to-viewport': {
      viewportWidth: 375,
      viewportUnit: 'vw',
      minPixelValue: 1,
      mediaQuery: false
    }
  }
}

