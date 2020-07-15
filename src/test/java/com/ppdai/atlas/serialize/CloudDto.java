package com.ppdai.atlas.serialize;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import org.joda.time.DateTime;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Objects;

/**
 * CloudDto
 */
@Validated
@javax.annotation.Generated(value = "com.ppdai.codegen.languages.PpdaiSpringClientCodegen", date = "2017-11-22T15:52:50.156+08:00")

public class CloudDto {
    @JsonProperty("description")
    private String description = null;

    @JsonProperty("extensions")
    private Object extensions = null;

    @JsonProperty("galaxy")
    private String galaxy = null;

    @JsonProperty("id")
    private Long id = null;

    @JsonProperty("insertBy")
    private String insertBy = null;

    @JsonProperty("insertTime")
    @JsonSerialize(using = MyDateSerializer.class)
    @JsonDeserialize(using = MyDateDeserializer.class)
    private DateTime insertTime = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("updateBy")
    private String updateBy = null;

    @JsonProperty("updateTime")
    private DateTime updateTime = null;

    public CloudDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     **/
    @ApiModelProperty(value = "")


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CloudDto extensions(Object extensions) {
        this.extensions = extensions;
        return this;
    }

    /**
     * Get extensions
     *
     * @return extensions
     **/
    @ApiModelProperty(value = "")


    public Object getExtensions() {
        return extensions;
    }

    public void setExtensions(Object extensions) {
        this.extensions = extensions;
    }

    public CloudDto galaxy(String galaxy) {
        this.galaxy = galaxy;
        return this;
    }

    /**
     * Get galaxy
     *
     * @return galaxy
     **/
    @ApiModelProperty(value = "")


    public String getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(String galaxy) {
        this.galaxy = galaxy;
    }

    public CloudDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @ApiModelProperty(value = "")


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CloudDto insertBy(String insertBy) {
        this.insertBy = insertBy;
        return this;
    }

    /**
     * Get insertBy
     *
     * @return insertBy
     **/
    @ApiModelProperty(value = "")


    public String getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(String insertBy) {
        this.insertBy = insertBy;
    }

    public CloudDto insertTime(DateTime insertTime) {
        this.insertTime = insertTime;
        return this;
    }

    /**
     * Get insertTime
     *
     * @return insertTime
     **/
    @ApiModelProperty(value = "")

    @Valid

    public DateTime getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(DateTime insertTime) {
        this.insertTime = insertTime;
    }

    public CloudDto name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get name
     *
     * @return name
     **/
    @ApiModelProperty(value = "")


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CloudDto updateBy(String updateBy) {
        this.updateBy = updateBy;
        return this;
    }

    /**
     * Get updateBy
     *
     * @return updateBy
     **/
    @ApiModelProperty(value = "")


    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public CloudDto updateTime(DateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * Get updateTime
     *
     * @return updateTime
     **/
    @ApiModelProperty(value = "")

    @Valid

    public DateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(DateTime updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CloudDto cloudDto = (CloudDto) o;
        return Objects.equals(this.description, cloudDto.description) &&
                Objects.equals(this.extensions, cloudDto.extensions) &&
                Objects.equals(this.galaxy, cloudDto.galaxy) &&
                Objects.equals(this.id, cloudDto.id) &&
                Objects.equals(this.insertBy, cloudDto.insertBy) &&
                Objects.equals(this.insertTime, cloudDto.insertTime) &&
                Objects.equals(this.name, cloudDto.name) &&
                Objects.equals(this.updateBy, cloudDto.updateBy) &&
                Objects.equals(this.updateTime, cloudDto.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, extensions, galaxy, id, insertBy, insertTime, name, updateBy, updateTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CloudDto {\n");

        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    extensions: ").append(toIndentedString(extensions)).append("\n");
        sb.append("    galaxy: ").append(toIndentedString(galaxy)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    insertBy: ").append(toIndentedString(insertBy)).append("\n");
        sb.append("    insertTime: ").append(toIndentedString(insertTime)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    updateBy: ").append(toIndentedString(updateBy)).append("\n");
        sb.append("    updateTime: ").append(toIndentedString(updateTime)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

