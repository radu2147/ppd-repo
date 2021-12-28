package objectProtocol;

import domain.VanzareDTO;

public class VanzareRequest implements Request{
    private VanzareDTO VanzareDTO;

    public VanzareRequest(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }

    public VanzareDTO getVanzareDTO() {
        return VanzareDTO;
    }

    public void setVanzareDTO(VanzareDTO VanzareDTO) {
        this.VanzareDTO = VanzareDTO;
    }
}
