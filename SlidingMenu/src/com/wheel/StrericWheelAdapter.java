/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wheel;


/**
 * Numeric Wheel adapter.
 */
public class StrericWheelAdapter implements WheelAdapter {
	
	/** The default min value */
	private String[] strContents;
	/**
	 * 
	 * @param strContents
	 */
	public StrericWheelAdapter(String[] strContents){
		this.strContents=strContents;
	}
	
	
	public String[] getStrContents() {
		return strContents;
	}


	public void setStrContents(String[] strContents) {
		this.strContents = strContents;
	}


	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			return strContents[index];
		}
		return null;
	}
	
	public int getItemsCount() {
		return strContents.length;
	}
	/**
	 * 璁剧疆鏈�ぇ鐨勫搴�
	 */
	public int getMaximumLength() {
		int maxLen=5;
		return maxLen;
	}
}
