package es.deusto.sd.verificacionfacial;

public class VerificadorBiometrico {

    //Diferencia maxima permitida entre los valores
    public static final double TOLERANCIA = 0.02;

    public boolean verificar(
            double[] vectorReferencia,
            double[] vectorCapturado) {

        //Comprobar si alguno de los vectores es nulo
        if (vectorReferencia == null || vectorCapturado == null) {
            return false;
        }

        //Comprobar si estan vacios o tienen distinta longitud
        if (vectorReferencia.length == 0
                || vectorReferencia.length != vectorCapturado.length) {
            return false;
        }

        //Recorrer los dos vectores y comparar cada posicion
        for (int i = 0; i < vectorReferencia.length; i++) {
            double referencia = vectorReferencia[i];
            double capturado = vectorCapturado[i];

            //Comprobar que los valores no sean NaN ni infinitos
            if (!Double.isFinite(referencia) || !Double.isFinite(capturado)) {
                return false;
            }

            //Comprobar si la diferencia supera la tolerancia
            if (Math.abs(referencia - capturado) > TOLERANCIA) {
                return false;
            }
        }

        //Devolver true si todos los valores cumplen la tolerancia
        return true;
    }

}
