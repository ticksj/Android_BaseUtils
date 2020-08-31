## Double 格式化

 DecimalFormat formater = newDecimalFormat("#0.##"); System.out.println(formater.format(123456.7897456));# 与 0 的区别：#：没有则为空0：没有则补0​
小数位保留
//使用0.00不足位补0，#.##仅保留有效位return new DecimalFormat("0.00").format(num);