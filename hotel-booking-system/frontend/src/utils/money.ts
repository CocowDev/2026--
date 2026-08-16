/**
 * 金额格式化工具
 * 后端金额统一为 BigDecimal（JSON 输出 number），展示时保留两位小数并千分位分隔
 */
export const formatMoney = (value: number | string | null | undefined): string => {
  const num = Number(value ?? 0);
  if (Number.isNaN(num)) {
    return '0.00';
  }
  return num.toLocaleString('zh-CN', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
};
