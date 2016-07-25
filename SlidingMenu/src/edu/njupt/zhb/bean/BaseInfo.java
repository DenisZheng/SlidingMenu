/**
 * 
 */
/**
 * @author Administrator
 *
 */
package edu.njupt.zhb.bean;
import java.util.Map;

public abstract class BaseInfo {
	private DataModel data;

	public DataModel getData() {
		return data;
	}

	public void setData(DataModel data) {
		this.data = data;
	}

	private Map tags;

	public Map getTags() {
		return tags;
	}

	public void setTags(Map tags) {
		this.tags = tags;
	}

	private int imageId;

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	private int orderby;

	public int getOrderBy() {
		return orderby;
	}

	public void setOrderBy(int orderby) {
		this.orderby = orderby;
	}
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	private String otherName;

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}
	private String UiImg;

	public String getUiImg() {
		return UiImg;
	}

	public void setUiImg(String UiImg) {
		this.UiImg = UiImg;
	}
	public void Init(DataModel mode, int imgId, String _id) {
		// TODO Auto-generated method stub
		this.data = mode;
		this.imageId = imgId;
		this.id =_id;
	}

	public abstract void InitMap(DataModel mode);

	private Object image;

	public Object getImage() {
		return image;
	}

	public void setImage(Object image) {
		this.image = image;
	}
	private Object image_b;

	public Object getImage_b() {
		return image_b;
	}

	public void setImage_b(Object image_b) {
		this.image_b = image_b;
	}
}