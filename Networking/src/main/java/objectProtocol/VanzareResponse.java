package objectProtocol;

import domain.VanzareDTO;

public class VanzareResponse implements Response{
    private VanzareDTO VanzareDTO;

    public VanzareResponse(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }

    public VanzareDTO getVanzareDTO() {
        return VanzareDTO;
    }

    public void setVanzareDTO(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }
}
