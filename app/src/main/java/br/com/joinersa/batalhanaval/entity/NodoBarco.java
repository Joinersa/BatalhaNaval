package br.com.joinersa.batalhanaval.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by joine on 04/09/2017.
 */

public class NodoBarco implements Parcelable {
    private int tipo;
    private int orientacao; // zero para horizontal | um para vertical
    private int parte;
    private boolean selecionado = false;

    public NodoBarco(int tipo, int orientacao, int parte) {
        this.tipo = tipo;
        this.orientacao = orientacao;
        this.parte = parte;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getOrientacao() {
        return orientacao;
    }

    public void setOrientacao(int orientacao) {
        this.orientacao = orientacao;
    }

    public int getParte() {
        return parte;
    }

    public void setParte(int parte) {
        this.parte = parte;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    @Override
    public String toString() {
        return "NodoBarco{" +
                "tipo=" + tipo +
                ", orientacao=" + orientacao +
                ", parte=" + parte +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.tipo);
        dest.writeInt(this.orientacao);
        dest.writeInt(this.parte);
        dest.writeByte(this.selecionado ? (byte) 1 : (byte) 0);
    }

    protected NodoBarco(Parcel in) {
        this.tipo = in.readInt();
        this.orientacao = in.readInt();
        this.parte = in.readInt();
        this.selecionado = in.readByte() != 0;
    }

    public static final Parcelable.Creator<NodoBarco> CREATOR = new Parcelable.Creator<NodoBarco>() {
        @Override
        public NodoBarco createFromParcel(Parcel source) {
            return new NodoBarco(source);
        }

        @Override
        public NodoBarco[] newArray(int size) {
            return new NodoBarco[size];
        }
    };
}
