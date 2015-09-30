package org.safehaus.model;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.impetus.kundera.index.Index;
import com.impetus.kundera.index.IndexCollection;

import static org.safehaus.Constants.DATABASE_SCHEMA;


@Entity
@Table( name = "document", schema = DATABASE_SCHEMA )
@IndexCollection( columns = {
        @Index( name = "ownerName" ), @Index( name = "ownerId" ), @Index( name = "documentName" )
} )
public class DocumentInfo
{
    @Id
    @Column( name = "document_id" )
    //    @TableGenerator( name = "id_gen", allocationSize = 30, initialValue = 100 )
    //    @GeneratedValue( /*generator = "id_gen", */strategy = GenerationType.TABLE )
    private String key;

    @Column( name = "employee_name" )
    private String ownerName;

    @Column( name = "employee_id" )
    private int ownerId;

    @Column( name = "uploaded_date" )
    private Date uplodedDate;

    @Column( name = "document_format" )
    private DataFormat dataFormat;

    @Column( name = "document_name" )
    private String documentName;

    @Column( name = "data" )
    private byte[] data;

    @Column( name = "size" )
    private long size;


    /**
     * @return the id
     */
    public String getKey()
    {
        return key;
    }


    /**
     * @param id the id to set
     */
    public void setKey( String id )
    {
        this.key = id;
    }


    /**
     * @return the ownerName
     */
    public String getOwnerName()
    {
        return ownerName;
    }


    /**
     * @param ownerName the ownerName to set
     */
    public void setOwnerName( String ownerName )
    {
        this.ownerName = ownerName;
    }


    /**
     * @return the ownerId
     */
    public int getOwnerId()
    {
        return ownerId;
    }


    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId( int ownerId )
    {
        this.ownerId = ownerId;
    }


    /**
     * @return the uplodedDate
     */
    public Date getUplodedDate()
    {
        return uplodedDate;
    }


    /**
     * @param uplodedDate the uplodedDate to set
     */
    public void setUplodedDate( Date uplodedDate )
    {
        this.uplodedDate = uplodedDate;
    }


    /**
     * @return the dataFormat
     */
    public DataFormat getDataFormat()
    {
        return dataFormat;
    }


    /**
     * @param dataFormat the dataFormat to set
     */
    public void setDataFormat( DataFormat dataFormat )
    {
        this.dataFormat = dataFormat;
    }


    public byte[] getData()
    {
        return data;
    }


    public void setData( byte[] data )
    {
        this.data = data;
    }


    public String getDocumentName()
    {
        return documentName;
    }


    public void setDocumentName( String documentName )
    {
        this.documentName = documentName;
    }


    public long getSize()
    {
        return size;
    }


    public void setSize( long size )
    {
        this.size = size;
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this ).append( "key", key ).append( "ownerName", ownerName )
                                          .append( "ownerId", ownerId ).append( "uplodedDate", uplodedDate )
                                          .append( "dataFormat", dataFormat ).append( "documentName", documentName )
                                          .append( "data", data ).append( "size", size ).toString();
    }
}
