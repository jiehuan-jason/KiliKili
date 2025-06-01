package top.jiehuan.kilikili.util;

public class FindString {
	// 循环查找字符串函数 需要手动在传入的keyword内加入双引号
		 public static String[] extractContents(String input, String keyword) {
			    String[] resultArray = new String[100];
		        int keywordLength = keyword.length()+1;
		        int currentIndex = 0;
		        int num=0;
		        //input="\""+input+"\"";

		        // 循环查找字符串
		        while ((currentIndex = input.indexOf(keyword, currentIndex)) != -1) {
		            // 查找第一个双引号
		            int startQuote = input.indexOf("\"", currentIndex + keywordLength);
		            if (startQuote == -1) {
		                break;  // 如果没有找到双引号，退出循环
		            }

		            // 查找第二个双引号
		            int endQuote = input.indexOf("\"", startQuote + 1);
		            if (endQuote == -1) {
		                break;  // 如果没有找到结束双引号，退出循环
		            }

		            // 提取双引号内的内容
		            String content = input.substring(startQuote + 1, endQuote);
		            resultArray[num]=content;// 将内容存入数组
		            num++;

		            // 更新currentIndex以查找下一个字符串
		            currentIndex = endQuote + 1;
		        }
		        return resultArray;
		    }
		 public static String[] extractContentsInt(String input, String keyword) {
			    String[] resultArray = new String[100];
		        int currentIndex = 0;
		        int num=0;
		        //input="\""+input+"\"";

		        // 循环查找字符串
		        while ((currentIndex = input.indexOf(keyword, currentIndex)) != -1) {
		            // 查找第一个双引号
		            int startQuote = input.indexOf(":", currentIndex);
		            if (startQuote == -1) {
		                break;  // 如果没有找到，退出循环
		            }

		            // 查找第二个双引号
		            int endQuote = input.indexOf(",", startQuote + 1);
		            if (endQuote == -1) {
		            	endQuote = input.indexOf("}", startQuote + 1);
		            	if(endQuote==-1)
		            		break;  // 如果没有找到结束，退出循环
		            }
		            

		            // 提取双引号内的内容
		            String content = input.substring(startQuote + 1, endQuote);
		            resultArray[num]=content;// 将内容存入数组
		            num++;

		            // 更新currentIndex以查找下一个字符串
		            currentIndex = endQuote + 1;
		        }
		        return resultArray;
		    }
		 public static String extractArraysAndDelete(String input, String keyword) {
		        int currentIndex = 0;
		        //input="\""+input+"\"";

		        // 循环查找字符串
		        while ((currentIndex = input.indexOf(keyword, currentIndex)) != -1) {
		            int startQuote = input.indexOf("[", currentIndex);
		            if (startQuote == -1) {
		                break;  // 如果没有找到，退出循环
		            }

		            int endQuote = input.indexOf("]", startQuote + 1);
		            if(endQuote==-1)
		            	endQuote = startQuote+100;
		            //由于未知的问题，这一段必须删除，所以不匹配回中括号
		            
		            

		            input = removeRange(input, startQuote + 1, endQuote);

		            // 更新currentIndex以查找下一个数组
		            currentIndex = endQuote + 1;
		        }
		        return input;
		    }
		 public static String[] FindTitleAndDeleteHtmlCode(String input) {
			 	String keyword="\"title\"";
			    String[] resultArray = new String[40];
		        //int keywordLength = keyword.length()+2;
		        int currentIndex = 0;
		        int num=0;
		        //input="\""+input+"\"";

		        // 循环查找字符串
		        while ((currentIndex = input.indexOf(keyword, currentIndex)) != -1) {
		            // 查找冒号
		            int startQuote = input.indexOf(":", currentIndex);
		            if (startQuote == -1) {
		                break;  // 如果没有找到双引号，退出循环
		            }

		            // 查找逗号
		            int endQuote = input.indexOf(",", startQuote + 1);
		            if (endQuote == -1) {
		                break;  // 如果没有找到结束双引号，退出循环
		            }

		            // 提取双引号内的内容
		            String content = input.substring(startQuote + 1, endQuote);
		            if(!content.equals("\"\"")){
		            	if (content.indexOf("<em class=\\\"keyword\\\">") != -1) {
		                    // 替换子串
		                    content = replace(content,"<em class=\\\"keyword\\\">", "");
		                    
		                }
		            	if (content.indexOf("</em>") != -1) {
		                    // 替换子串
		                    content = replace(content,"</em>", "");
		                    
		                }
		            	resultArray[num]=content.substring(1, content.length()-1);// 将内容存入数组
		            	num++;
		            }
		            
		            

		            // 更新currentIndex以查找下一个字符串
		            currentIndex = endQuote + 1;
		        }
		        return resultArray;
		    }
		 public static String[] Display_Desc(String str){
			 
			 	int count = 0;
		        int start = 0;
		        int end = 0;

		        String delimiter="\\n";
				// 计算分割后的字符串数量
		        while ((end = str.indexOf(delimiter, start)) != -1) {
		            count++;
		            start = end + 2;
		        }
		        count++; // 最后一个元素
		        System.out.println(count);
		        String[] result = new String[count];
		        start = 0;
		        int index = 0;

		        // 进行分割
		        while ((end = str.indexOf(delimiter, start)) != -1) {
		            result[index++] = str.substring(start, end);
		            start = end + 2;
		        }
		        result[index] = str.substring(start); // 添加最后一个元素
		        
		        for (int i = 0; i < result.length; i++) {
		            System.out.println(result[i]);
		        }
		        return result;

		 }
		 public static String findValue(String jsonString, String findKey) {
			    // 找到指定字段的索引
			    int titleIndex = jsonString.indexOf("\"" + findKey + "\"");
			    if (titleIndex == -1) {
			        System.out.println("No Find Text:"+findKey);
			        return null; // 如果没有找到指定字段，返回 null
			    }

			    // 找到第一个双引号的位置
			    int firstQuoteIndex = jsonString.indexOf("\"", titleIndex + findKey.length() + 2); // +2 是为了跳过字段名和后面的引号
			    if (firstQuoteIndex == -1) {
			        System.out.println("No Find first");
			        return null; // 如果没有找到下一个双引号，返回 null
			    }

			    // 找到第二个双引号的位置
			    int secondQuoteIndex = jsonString.indexOf("\"", firstQuoteIndex + 1);
			    if (secondQuoteIndex == -1) {
			        System.out.println("No Find Second");
			        return null; // 如果没有找到第二个双引号，返回 null
			    }

			    // 提取并返回指定字段的值
			    return jsonString.substring(firstQuoteIndex + 1, secondQuoteIndex);
			}
			
			
			public static String findValueInt(String jsonString, String findText){
				int titleIndex = jsonString.indexOf("\"" + findText + "\"");
			    if (titleIndex == -1) {
			        System.out.println("No Find Text:"+findText);
			        return null; // 如果没有找到指定字段，返回 null
			    }

			    // 找到第一个双引号的位置
			    int firstQuoteIndex = jsonString.indexOf(":", titleIndex + findText.length() + 2); // +2 是为了跳过字段名和后面的引号
			    if (firstQuoteIndex == -1) {
			        System.out.println("No Find first");
			        return null; // 如果没有找到下一个双引号，返回 null
			    }

			    // 找到第二个双引号的位置
			    int secondQuoteIndex = jsonString.indexOf(",", firstQuoteIndex + 1);
			    if (secondQuoteIndex == -1) {
			    	secondQuoteIndex = jsonString.indexOf("}", firstQuoteIndex + 1);
			    	if(secondQuoteIndex==-1){
			    		System.out.println("No Find Second");
			    		return null; // 如果没有找到第二个双引号，返回 null
			    	}
			    }

			    // 提取并返回指定字段的值
			    return jsonString.substring(firstQuoteIndex + 1, secondQuoteIndex);
			}
			
			public static String findValueBool(String jsonString, String findText){
				return findValueInt(jsonString, findText);
			}
			
			public static String findValueInCookies(String jsonString, String findText){
				int titleIndex = jsonString.indexOf(findText);
			    if (titleIndex == -1) {
			        System.out.println("No Find Text:"+findText);
			        return null; // 如果没有找到指定字段，返回 null
			    }

			    int firstQuoteIndex = jsonString.indexOf("=", titleIndex + findText.length());
			    if (firstQuoteIndex == -1) {
			        System.out.println("No Find first");
			        return null; 
			    }

			    int secondQuoteIndex = jsonString.indexOf(";", firstQuoteIndex + 1);
			    if (secondQuoteIndex == -1) {
			    	secondQuoteIndex = jsonString.indexOf(",", firstQuoteIndex + 1);
			    	if (secondQuoteIndex == -1){
			    		System.out.println("No Find Second");
			    		return null; // 如果没有找到分号，返回 null
			    	}
			    }

			    return jsonString.substring(firstQuoteIndex + 1, secondQuoteIndex);
			}
			public static String findValueInHTMLLabel(String htmlString){
				String findText = "<div id=\"1-name\">";
				int titleIndex = htmlString.indexOf(findText);
			    if (titleIndex == -1) {
			        System.out.println("No Find Text:"+findText);
			        return null; // 如果没有找到指定字段，返回 null
			    }

			    int firstQuoteIndex = htmlString.indexOf(">", titleIndex + findText.length()-1);
			    if (firstQuoteIndex == -1) {
			        System.out.println("No Find first");
			        return null; 
			    }

			    int secondQuoteIndex = htmlString.indexOf("<", firstQuoteIndex + 1);
			    if (secondQuoteIndex == -1) {
			    	System.out.println("No Find Second");
		    		return null; 
			    }

			    return htmlString.substring(firstQuoteIndex + 1, secondQuoteIndex);
			}
			public static String replace(String original, String toReplace, String replacement) {
			    StringBuffer result = new StringBuffer();
			    int index = 0;
			    int toReplaceLength = toReplace.length();

			    while ((index = original.indexOf(toReplace, index)) != -1) {
			        result.append(original.substring(0, index)); // 添加未替换部分
			        result.append(replacement); // 添加替换部分
			        index += toReplaceLength; // 移动索引
			        original = original.substring(index); // 更新原始字符串
			        index = 0; // 重置索引
			    }
			    result.append(original); // 添加剩余部分

			    return result.toString();
			}
			public static String removeRange(String input, int start, int end) {
			    if (input == null || start < 0 || end > input.length() || start >= end) {
			        return input;
			    }
			    return input.substring(0, start) + input.substring(end);
			}
}
