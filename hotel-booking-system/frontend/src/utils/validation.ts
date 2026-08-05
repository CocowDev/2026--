export const validateEmail = (email: string): boolean => {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return regex.test(email)
}

export const validatePassword = (password: string): { valid: boolean; message: string } => {
  if (password.length < 6) {
    return { valid: false, message: '密码至少需要6个字符' }
  }
  if (!/[a-zA-Z]/.test(password)) {
    return { valid: false, message: '密码需要包含字母' }
  }
  if (!/[0-9]/.test(password)) {
    return { valid: false, message: '密码需要包含数字' }
  }
  return { valid: true, message: '' }
}

export const validateUsername = (username: string): { valid: boolean; message: string } => {
  if (username.length < 3) {
    return { valid: false, message: '用户名至少需要3个字符' }
  }
  if (!/^[a-zA-Z0-9_]+$/.test(username)) {
    return { valid: false, message: '用户名只能包含字母、数字和下划线' }
  }
  return { valid: true, message: '' }
}

export const validateDateRange = (checkIn: string, checkOut: string): { valid: boolean; message: string } => {
  if (!checkIn || !checkOut) {
    return { valid: false, message: '请选择入住和退房日期' }
  }
  const inDate = new Date(checkIn)
  const outDate = new Date(checkOut)
  if (outDate <= inDate) {
    return { valid: false, message: '退房日期必须晚于入住日期' }
  }
  return { valid: true, message: '' }
}

export const validatePhone = (phone: string): boolean => {
  const regex = /^1[3-9]\d{9}$/
  return regex.test(phone)
}

export interface ValidationRule {
  required?: boolean
  message?: string
  validator?: (value: any) => { valid: boolean; message: string }
}

export const validateForm = (fields: Record<string, any>, rules: Record<string, ValidationRule>): { isValid: boolean; errors: Record<string, string> } => {
  const errors: Record<string, string> = {}
  let isValid = true
  
  for (const [field, value] of Object.entries(fields)) {
    if (rules[field]?.required && !value) {
      errors[field] = rules[field].message || `${field}不能为空`
      isValid = false
    }
    if (rules[field]?.validator && value) {
      const result = rules[field].validator(value)
      if (!result.valid) {
        errors[field] = result.message
        isValid = false
      }
    }
  }
  
  return { isValid, errors }
}