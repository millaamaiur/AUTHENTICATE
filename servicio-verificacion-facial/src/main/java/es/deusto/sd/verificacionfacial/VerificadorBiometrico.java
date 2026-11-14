package es.deusto.sd.verificacionfacial;

public class VerificadorBiometrico {

    public static final double TOLERANCIA = 0.02;

    public boolean verificar(
            double[] vectorReferencia,
            double[] vectorCapturado) {

        //Manejar casos no permitidos
        if (vectorReferencia == null || vectorCapturado == null) {
            return false;
        }

        if (vectorReferencia.length == 0
                || vectorReferencia.length != vectorCapturado.length) {
            return false;
        }

        //Recorrer el vector y comprobarlo
        for (int i = 0; i < vectorReferencia.length; i++) {
            double referencia = vectorReferencia[i];
            double capturado = vectorCapturado[i];

            //Comprobamos si solo es un double o no
            if (!Double.isFinite(referencia) || !Double.isFinite(capturado)) {
                return false;
            }

            if (Math.abs(referencia - capturado) > TOLERANCIA) {
                return false;
            }
        }

        return true;
    }

}
