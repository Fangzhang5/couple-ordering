function formatPrice(price) {
  const value = Number(price)

  if (!Number.isFinite(value)) {
    return "0.00"
  }

  return value.toFixed(2)
}

module.exports = {
  formatPrice
}