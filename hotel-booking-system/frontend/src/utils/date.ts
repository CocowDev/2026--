export const formatDate = (date: Date | string, format = 'YYYY-MM-DD'): string => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year.toString())
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

export const formatDateTime = (date: Date | string): string => {
  return formatDate(date, 'YYYY-MM-DD HH:mm:ss')
}

export const formatDateCN = (date: Date | string): string => {
  const d = new Date(date)
  const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
  const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' }
  return `${d.toLocaleDateString('zh-CN', options)} ${weekdays[d.getDay()]}`
}

export const getCurrentDateTime = () => {
  return {
    date: formatDateCN(new Date()),
    time: formatDate(new Date(), 'HH:mm:ss')
  }
}

export const getDaysBetween = (startDate: string, endDate: string): number => {
  const start = new Date(startDate)
  const end = new Date(endDate)
  const diffTime = Math.abs(end.getTime() - start.getTime())
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays
}

export const addDays = (date: Date | string, days: number): Date => {
  const result = new Date(date)
  result.setDate(result.getDate() + days)
  return result
}

export const getToday = (): string => {
  return formatDate(new Date())
}

export const getTomorrow = (): string => {
  return formatDate(addDays(new Date(), 1))
}

export const isFutureDate = (date: string): boolean => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  const target = new Date(date)
  target.setHours(0, 0, 0, 0)
  return target >= today
}