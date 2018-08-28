package ec.edu.espol.mdleiton.saludlaboral;

public class Aviso {
    private int imageId;
    private String categoria;
    private String sugerencias;
    private String fecha;

    public Aviso(int imageId, String categoria, String sugerencias,String fecha) {
        this.imageId = imageId;
        this.categoria = categoria;
        this.sugerencias = sugerencias;
        this.fecha = fecha;
    }
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSugerencias() {
        return sugerencias;
    }

    public void setSugerencias(String sugerencias) {
        this.sugerencias = sugerencias;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return categoria + "\n" + sugerencias;
    }
}